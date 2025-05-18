package library.lv4.controller.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import library.App;
import library.AppException;
import library.lv3.usecase.AddNewBookInteractor;
import library.lv3.usecase.dto.BookDto;
import library.lv4.controller.mapper.Mapper;
import lombok.Data;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/book/add")
public class AddNewBookServlet extends HttpServlet {
    private AddNewBookInteractor addNewBookInteractor;
    private Mapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        addNewBookInteractor = App.ADD_NEW_BOOK_INTERACTOR;
        mapper = App.mapper;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var msg = req.getReader().lines().collect(Collectors.joining(" "));
            var request = mapper.deserialize(msg, Request.class);
            addNewBookInteractor.add(request.getBook(), request.getAuthorIds());
            resp.setStatus(200);
            resp.setContentType(mapper.getContentType());
            resp.getWriter().println("{}");
        } catch (AppException e) {
            resp.setStatus(400);
            resp.setContentType(mapper.getContentType());
            resp.getWriter().println(mapper.serialize(new ErrorResponse(e.getMessage())));
        }
    }

    @Data
    public static class Request {
        private BookDto book;
        private List<Long> authorIds;
    }
}
