package com.ssafy.vacsa.controller;

import com.ssafy.vacsa.dto.*;
import com.ssafy.vacsa.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    
    @ApiOperation(value = "게시글 생성", notes = "게시글 정보를 입력한다.")
    @PostMapping("/create")
    public ResponseEntity<Map<String,Object>> createVote(@RequestBody BoardDto boardDto) throws Exception {
        System.out.println(boardDto.getBoardTitle());
        System.out.println(boardDto.getBoardContent());
        System.out.println(boardDto.getAuthor());
        Long boardId = boardService.create(boardDto);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("message",SUCCESS);
        resultMap.put("boardId",boardId);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);  // status 200과 success라는 문자열을 반환
    }

    @ApiOperation(value = "게시글 삭제", notes = "해당 게시글을 삭제한다.")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable @ApiParam(value = "게시글의 id", required = true) Long boardId) throws Exception {
        boardService.delete(boardId);
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    }

//    @ApiOperation(value = "게시글 상세 페이지", notes = "해당 게시글 상세정보를 불러온다.")
//    @GetMapping("/{boardId}")
//    public ResponseEntity<BoardDto> detailVote(@PathVariable @ApiParam(value = "게시글의 id", required = true) Long boardId,@RequestParam @ApiParam(value = "로그인 유저 id", required = true) String username) throws Exception {
//        return new ResponseEntity<BoardDto>(boardService.detail(boardId,username), HttpStatus.OK);
//    }

    // 상세페이지 확인 시 username 필요 없게 변경
    @ApiOperation(value = "게시글 상세 페이지", notes = "해당 게시글 상세정보를 불러온다.")
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto> detailVote(@PathVariable @ApiParam(value = "게시글의 id", required = true) Long boardId) throws Exception {
        return new ResponseEntity<BoardDto>(boardService.boardDetail(boardId), HttpStatus.OK);
    }

//    @ApiOperation(value = "게시글 전체목록")
//    @GetMapping
//    public ResponseEntity<List<BoardDto>> getVoteList(@RequestParam(required = false) @ApiParam(value = "로그인한 유저 아이디") String username) throws Exception{
//        List<BoardDto> list = boardService.getBoardList(username);
//        return new ResponseEntity<>(list,HttpStatus.OK);
//    }

    @ApiOperation(value = "게시글 전체목록")
    @GetMapping
    public ResponseEntity<List<BoardDto>> getBoardList() throws Exception{
        List<BoardDto> list = boardService.getBoardListTotal();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글 수정")
    @PutMapping("/update")
    public ResponseEntity<String> modifyBoard(@RequestBody @ApiParam(value = "수정한 게시글 정보", required = true) BoardDto boardDto) throws  Exception{
        logger.info("modifyBoard 호출 "+boardDto.getBoardId()+" "+boardDto.getBoardContent());
        System.out.println(boardDto.getBoardId()+boardDto.getBoardTitle()+boardDto.getBoardContent());
        String message = "";
        HttpStatus status = null;
        try {
            boardService.modifyBoard(boardDto);
            message = SUCCESS;
            status = HttpStatus.OK;
        }catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = FAIL;
        }
        return new ResponseEntity<>(message, status);
    }
}
