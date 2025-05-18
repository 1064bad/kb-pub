package server.controller.render;

import server.controller.Response;
import server.controller.Model;

public interface Render {
    Response render(Model model);
}
