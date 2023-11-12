package MatchingMaker.MatchingMaker.service;

import MatchingMaker.MatchingMaker.domain.Board.Board;
import MatchingMaker.MatchingMaker.domain.Board.BoardFile;
import MatchingMaker.MatchingMaker.dto.BoardDTO;
import MatchingMaker.MatchingMaker.repository.Board.BoardFileRepository;
import MatchingMaker.MatchingMaker.repository.Board.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO -> domain (domain Class)
// domain -> DTO (DTO Class)
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    public void save(BoardDTO boardDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리
        if (boardDTO.getBoardFile().isEmpty()) {
            // 첨부 파일 없음.
            Board board = Board.toSave(boardDTO);
            boardRepository.save(board);
        } else {
            // 첨부 파일 있음.
            /*
                1. DTO에 담긴 파일을 꺼냄
                2. 파일의 이름 가져옴
                3. 서버 저장용 이름을 만듦
                // 내사진.jpg => 839798375892_내사진.jpg
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table에 해당 데이터 save 처리
                7. board_file_table에 해당 데이터 save 처리
             */
            Board boardE = Board.toSaveFile(boardDTO);
            Long savedId = boardRepository.save(boardE).getId();
            Board board = boardRepository.findById(savedId).get();
            for (MultipartFile boardFile: boardDTO.getBoardFile()) {
                //    MultipartFile boardFile = boardDTO.getBoardFile(); // 1.
                String originalFilename = boardFile.getOriginalFilename(); // 2.
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3.
                String savePath = "D:/MM/Board/" + storedFileName; // 4. D:/MM/Board/9802398403948_내사진.jpg
                boardFile.transferTo(new File(savePath)); // 5.

                BoardFile boardFileE = BoardFile.toBoardFile(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileE);
            }
        }

    }

    @Transactional
    public List<BoardDTO> findAll() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board: boardList) {
            boardDTOList.add(BoardDTO.toBoardDTO(board));
        }
        return boardDTOList;
    }
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(board);
            return boardDTO;
        } else {
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO) {
        Board board = Board.toUpdate(boardDTO);
        boardRepository.save(board);
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 5; // 한 페이지에 보여줄 글 갯수
        // 한페이지당 5개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        Page<Board> boardE =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("board.getContent() = " + boardE.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("board.getTotalElements() = " + boardE.getTotalElements()); // 전체 글갯수
        System.out.println("board.getNumber() = " + boardE.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("board.getTotalPages() = " + boardE.getTotalPages()); // 전체 페이지 갯수
        System.out.println("board.getSize() = " + boardE.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("board.hasPrevious() = " + boardE.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("board.isFirst() = " + boardE.isFirst()); // 첫 페이지 여부
        System.out.println("board.isLast() = " + boardE.isLast()); // 마지막 페이지 여부

        // 목록: id, writer, title, hits, createdTime
        Page<BoardDTO> boardDTOS = boardE.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;
    }
}
