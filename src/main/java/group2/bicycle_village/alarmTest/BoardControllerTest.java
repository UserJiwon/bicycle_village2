package group2.bicycle_village.alarmTest;

import group2.bicycle_village.common.constant.CommonCode;
import group2.bicycle_village.common.dto.BoardDTO;
import group2.bicycle_village.common.dto.BoardEntity;
import group2.bicycle_village.controller.Controller;
import group2.bicycle_village.controller.ModelAndView;
import group2.bicycle_village.service.AlarmService;
import group2.bicycle_village.service.AlarmServiceImpl;
import group2.bicycle_village.service.BoardService;
import group2.bicycle_village.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


public class BoardControllerTest implements Controller {
	BoardService boardService = new BoardServiceImpl();
	AlarmService alarmService = new AlarmServiceImpl();

	public BoardControllerTest(){
		System.out.println("BoardController 생성자 호출...");
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		return null;
	}
	
	/**
	 *  전체검색
	 * */
	public ModelAndView selectAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
//		String pageNo = request.getParameter("pageNo");
//		if (pageNo == null || pageNo.equals("")) {
//			pageNo = "1";
//		}

		//List<BoardDTO> list = boardService.selectAll(Integer.parseInt(pageNo));
		List<BoardDTO> list = boardService.selectAll();
		
		request.setAttribute("list", list);// 뷰에서 ${list}
		//request.setAttribute("pageNo", pageNo); // 뷰에서 ${pageNo}

		return new ModelAndView("board/freeBoard/freeBoardList.jsp"); // forward방식으로 이동
	}
	
	/**
	 * 등록하기
	 * */
	public ModelAndView insert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//전송된 데이터 받기
		int boardCount = Integer.parseInt(request.getParameter("board_count"));
		int goodsPrice = Integer.parseInt(request.getParameter("goods_price"));
		//HttpSession session = request.getSession(); //controller에서 현재 로그인한 세션값 얻어오기
		long userSeq = Integer.parseInt(request.getParameter("user_seq"));
		String boardName = request.getParameter("board_name");
		String category = request.getParameter("category");
		String isSeen = request.getParameter("is_seen");
		String boardContent = request.getParameter("board_content");
		String boardAddr = request.getParameter("board_addr");
		long productSeq = Integer.parseInt(request.getParameter("product_seq"));
		
		BoardEntity board = new BoardEntity.Builder().price(goodsPrice).userSeq(userSeq).boardCount(boardCount)
				.boardName(boardName).category(CommonCode.BoardCategory.valueOf(category))
				.isSeen(CommonCode.BoardStatus.valueOf(isSeen)).addr(boardAddr).content(boardContent)
				.productSeq(productSeq).build(); // 주소,내용,카테고리만 추가된 dto 생성하는데 카테고리 내용에 매칭되는 int 저장


		boardService.insert(board);
		long boardSeq = alarmService.searchBoardSeq(userSeq);
		String url = alarmService.linkURL(boardSeq);
		alarmService.setLinkURL(url);

		
		return new ModelAndView("front?key=board&methodName=selectAll", true);
		//return new ModelAndView("front?key=board&methodName=selectByBoardSeq&boardSeq=", true); // 나중에
		
	}
	
	/**
	 * 상세보기 
	 * */

	public ModelAndView selectByBoardSeq(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		 int boardSeq = Integer.parseInt(request.getParameter("boardSeq"));
		 boolean state = request.getParameter("flag")==null ? true : false;
		 
		String pageNo =  request.getParameter("pageNo");
		 
		 //두번째 인수 boolean 조회수 증가여부를판단할 인수(true이면, false이면 증가안함)
		BoardDTO board = boardService.selectByBoardSeq(boardSeq, state);
		request.setAttribute("board", board);
		request.setAttribute("pageNo", pageNo);
		
		return new ModelAndView("board/freeBoard/freeBoard.jsp"); //forward방식 
	}
	
	/**
	 *  수정폼
	 * */
	public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int boardSeq = Integer.parseInt(request.getParameter("boardSeq"));
		BoardDTO board = boardService.selectByBoardSeq(boardSeq, false);
		
		request.setAttribute("board", board);
		
		return new ModelAndView("board/freeBoard/freeBoardUpdate.jsp");//forward방식
	}
	
	/**
	 * 수정완료
	 * */
	public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 전송된 데이터 받기
		long boardSeq = Integer.parseInt(request.getParameter("boardSeq"));
		String boardName = request.getParameter("board_name");
		String boardContent = request.getParameter("board_content");
		int goodsPrice = Integer.parseInt(request.getParameter("goods_price"));
		

		BoardEntity board = new BoardEntity.Builder().boardSeq(boardSeq).boardName(boardName).content(boardContent)
				.price(goodsPrice).build(); // 주소,내용,카테고리만 추가된 dto 생성하는데 카테고리 내용에 매칭되는 int 저장

		String url = alarmService.linkURL(boardSeq);
		boardService.update(board, url);
		
		//수정이 완료가 된후....
		ModelAndView mv = new ModelAndView();
		
		//mv.setViewName("front?key=board&methodName=selectByBoardSeq&boardSeq="+boardSeq);
		mv.setViewName("front?key=board&methodName=selectAll");

	    mv.setRedirect(true);
		return mv;
	}
	
	/**
	 * 삭제
	 * 
	 * */
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int boardSeq = Integer.parseInt(request.getParameter("boardSeq"));
		boardService.delete(boardSeq);
		
		return new ModelAndView("front?key=board&methodName=selectAll", true);
	}
}
