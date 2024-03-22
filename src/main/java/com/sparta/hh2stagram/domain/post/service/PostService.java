package com.sparta.hh2stagram.domain.post.service;

import com.sparta.hh2stagram.domain.follow.entity.Follow;
import com.sparta.hh2stagram.domain.follow.repository.FollowRepository;
import com.sparta.hh2stagram.domain.likes.repository.LikesRepository;
import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.CreatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.UpdatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto;
import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.post.entity.PostImage;
import com.sparta.hh2stagram.domain.post.repository.PostImageRepository;
import com.sparta.hh2stagram.domain.post.repository.PostRepository;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.repository.UserRepository;
import com.sparta.hh2stagram.global.aws.service.S3UploadService;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final S3UploadService s3UploadService;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    private final FollowRepository followRepository;

    // 게시물 등록
    @Transactional
    public PostResponseDto.PostsResponseDto createPost(CreatePostRequestDto requestDto, List<MultipartFile> multipartFileList, User user) throws IOException {

        Post post = requestDto.toEntity(user);

        postRepository.save(post);
        log.info("게시물 데이터베이스 저장 완료");

        List<PostImage> postImageList = saveImgToS3(multipartFileList, post);

        return getPostsResponseDto(post, postImageList);
    }

    // 게시물 수정 (글만 수정)
    @Transactional
    public PostResponseDto.PostsResponseDto updatePost(Long postId, UpdatePostRequestDto requestDto, User user) {

        // 현재 유저가 작성한 게시물인지 확인
        Post post = (Post) postRepository.findByIdAndUser(postId, user)
                .orElseThrow(() -> new CustomApiException("해당 게시물에 대한 권한이 없습니다."));

        post.update(requestDto);

        return getPostsResponseDto(post);
    }

    // 게시물 삭제 //게시물을 삭제할까요? 이 게시물을 삭제하시겠어요?
    @Transactional
    public void deletePost(Long postId, User user) {

        // 현재 유저가 작성한 게시물인지 확인
        Post post = (Post) postRepository.findByIdAndUser(postId, user)
                .orElseThrow(() -> new CustomApiException("해당 게시물에 대한 권한이 없습니다."));

        postRepository.delete(post);

    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto.PostsResponseDto> getPost(User user) {
        // 모든 게시글을 조회합니다.
        List<Post> allPosts = postRepository.findAll();

        // 만약 게시글이 존재하지 않는다면 CustomApiException을 던집니다.
        if (allPosts.isEmpty()) {
            throw new CustomApiException("해당 게시물이 존재하지 않습니다.");
        }

        return getPostsResponseDtoList(allPosts, user);
    }

    // 유저페이지
    @Transactional(readOnly = true)
    public PostResponseDto.UserPageResponseDto getPostByUsername(String username, User user) {
        User owner = userRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("존재하지 않는 사용자입니다.")
        );
        List<Post> posts = postRepository.findByUser(owner);


        // 전체 게시글 목록을 UserPageResponseDto로 변환하여 반환합니다.
        return PostResponseDto.UserPageResponseDto.builder()
                .posts(getPostsResponseDtoList(posts, user))
                .userInfo(PostResponseDto.UserInfoResponseDto.builder()
                        .follower(followRepository.findByFollowingUserId(owner.getId()).size())
                        .following(followRepository.findByFollower(owner).size())
                        .postNumber(posts.size())
                        .build()
                )
                .build();
    }

    // 팔로우한 사용자의 게시물 가져오기
    @Transactional(readOnly = true)
    public List<PostResponseDto.PostsResponseDto> getPostsOfFollowedUser(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new NullPointerException("존재하지 않는 사용자입니다.")
        );

        List<Post> posts = new ArrayList<>();
        List<Follow> followList = user.getFollowList();
        for (Follow follow : followList) {
            posts.addAll(postRepository.findByUserId(follow.getFollowingUserId()));
        }

        if (posts.isEmpty()) {
            throw new CustomApiException("게시물을 찾을 수 없습니다.");
        }

        return getPostsResponseDtoList(posts, user);
    }

    private PostResponseDto.PostsResponseDto getPostsResponseDto(Post post) {
        return PostResponseDto.PostsResponseDto.builder()
                .postId(post.getId())
                .username(post.getUser().getUsername())
                .contents(post.getContents())
                .postImageList(post.getPostImageList().stream().map(PostResponseDto.PostImageResponseDto::new).toList())
                .likes(post.getLikesList().size())
                .commentList(post.getCommentList())
                .createdAt(post.getCreatedAt())
                .build();
    }

    private PostResponseDto.PostsResponseDto getPostsResponseDto(Post post, List<PostImage> postImageList) {
        return PostResponseDto.PostsResponseDto.builder()
                .postId(post.getId())
                .username(post.getUser().getUsername())
                .contents(post.getContents())
                .postImageList(postImageList.stream().map(PostResponseDto.PostImageResponseDto::new).toList())
                .likes(0)
                .like(false)
                .commentList(new ArrayList<>())
                .createdAt(post.getCreatedAt())
                .build();
    }

    private List<PostResponseDto.PostsResponseDto> getPostsResponseDtoList(List<Post> posts, User user) {
        List<PostResponseDto.PostsResponseDto> responseDtoList = new ArrayList<>();
        for (Post post : posts) {
            responseDtoList.add(PostResponseDto.PostsResponseDto.builder()
                    .postId(post.getId())
                    .username(post.getUser().getUsername())
                    .contents(post.getContents())
                    .postImageList(post.getPostImageList().stream().map(PostResponseDto.PostImageResponseDto::new).toList())
                    .likes(post.getLikesList().size())
                    .like(likesRepository.findByUserAndPost(user, post).isPresent())
                    .commentList(post.getCommentList())
                    .createdAt(post.getCreatedAt())
                    .build());
        }

        return responseDtoList;
    }

    // AW3 관련 자료
    private List<PostImage> saveImgToS3(List<MultipartFile> multipartFileList, Post post) throws IOException {
        List<PostImage> postImageList = new ArrayList<>();

        // 전달된 MultipartFile 목록에서 각 파일을 순회합니다.
        for (MultipartFile multipartFile : multipartFileList) {

            // 이미지 파일 이름을 생성합니다. 중복을 피하기 위해 UUID를 이용합니다.
            String filename = UUID.randomUUID() + multipartFile.getOriginalFilename();

            // S3에 파일을 업로드하고, 업로드된 이미지의 URL을 받아옵니다.
            String imageUrl = s3UploadService.saveFile(multipartFile, filename);

            // 업로드된 이미지 정보를 저장하기 위해 PostImage 객체를 생성합니다.
            PostImage postImage = PostImage.builder()
                    .url(imageUrl) // 이미지의 URL을 설정합니다.
                    .imageName(multipartFile.getOriginalFilename()) // 이미지의 원래 이름을 설정합니다.
                    .s3name(filename) // S3에 저장된 파일의 이름을 설정합니다.
                    .post(post) // 해당 이미지가 속한 게시물을 설정합니다.
                    .build();

            // 생성된 PostImage 객체를 저장소에 저장합니다.
            postImageRepository.save(postImage);
            postImageList.add(postImage);
        }
        return postImageList;
    }
}
