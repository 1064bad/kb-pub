package server.controller.film;

import server.controller.HttpController;
import server.controller.Model;
import server.controller.Request;
import server.controller.UrlPath;

@HttpController
public class ActorController {
    @UrlPath("/actor/list")
    public Model list(Request request) {
        return new Model("actor_list");
    }
}
