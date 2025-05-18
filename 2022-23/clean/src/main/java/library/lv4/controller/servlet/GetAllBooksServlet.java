package library.lv4.controller.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import library.App;
import library.lv3.usecase.GetAllBooksInteractor;
import library.lv4.controller.mapper.Mapper;

import java.io.IOException;

@WebServlet("/book/ls")
public class GetAllBooksServlet extends HttpServlet {
    private GetAllBooksInteractor getAllBooksInteractor;
    private Mapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        getAllBooksInteractor = App.GET_ALL_BOOKS_INTERACTOR;
        mapper = App.mapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        resp.setContentType(mapper.getContentType());
        resp.getWriter().println(mapper.serialize(getAllBooksInteractor.get()));
    }
}
