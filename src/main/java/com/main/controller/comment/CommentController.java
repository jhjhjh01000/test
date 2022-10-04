package com.main.controller.comment;

import com.main.config.auth.PrincipalDetails;
import com.main.domain.comment.Comment;
import com.main.dto.CMRespDto;
import com.main.dto.comment.CommentDto;
import com.main.exception.CustomValidationApiException;
import com.main.service.comment.CommentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;


    /**
     *  댓글 생성
     */
    @PostMapping("/api/comment")
    public ResponseEntity commentSave(@Valid @RequestBody CommentDto commentDto, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails){
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            //for문을 돌면서 에러 생긴 필드에 관한 메세지를 모음
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }   //강제로 Exception이 터지도록하였다. 이렇게 Exception만 터지게되면 ux가 좋지않다.
            //그러므로 Exception만을 따로 처리하는 handler패키지 만듦
            throw new CustomValidationApiException("유효성 검사를 실패하였습니다", errorMap);
        }


        Comment comment  =  commentService.댓글쓰기(commentDto.getContent(),commentDto.getImageId(),principalDetails.getMember().getMemberId());  //여기에 날라가야할게 content,ImageId,UserId 세개가 날라갸야됨

        return new ResponseEntity<>(new CMRespDto<>("댓글 쓰기 성공",comment), HttpStatus.CREATED);
    }


    /**
     *  댓글 삭제
     */
    @DeleteMapping("/api/comment/{id}")
    public ResponseEntity commentDelete(@PathVariable Long id){
        commentService.댓글삭제(id);
        return new ResponseEntity<>(new CMRespDto<>("댓글삭제 성공",null),HttpStatus.OK);
    }



}
