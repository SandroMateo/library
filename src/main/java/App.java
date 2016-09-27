import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/login", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      String username = request.queryParams("username");
      String password = request.queryParams("password");
      for (int i = 0; i < Patron.all().size(); i++) {
        if(username.equals(Patron.all().get(i).getUsername())) {
          if(password.equals(Patron.all().get(i).getPassword())) {
            model.put("patron", Patron.all().get(i));
            model.put("books", Book.all());
            model.put("template", "templates/patron.vtl");
          } else {
          model.put("wrongPassword", true);
          model.put("template", "templates/index.vtl");
          }
        } else {
          model.put("wrongUsername", true);
          model.put("template", "templates/index.vtl");
        }
      }
      model.put("books", Book.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/create-account", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      model.put("template", "templates/create-account.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/patron/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      int id = Integer.parseInt(request.params("id"));
      model.put("patron", Patron.find(id));
      model.put("books", Book.all());
      model.put("template", "templates/patron.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/new-account", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      String username = request.queryParams("username");
      String password = request.queryParams("password");
      String name = request.queryParams("name");
      boolean librarian = Boolean.parseBoolean(request.queryParams("librarian"));
      boolean usernameTaken = false;
      for (int i = 0; i < Patron.all().size(); i++) {
        if(username.equals(Patron.all().get(i).getUsername())) {
          model.put("usernameTaken", true);
          model.put("template", "templates/create-account.vtl");
          usernameTaken = true;
        }
      }
      if (!usernameTaken) {
        Patron patron = new Patron(username, password, name, librarian);
        patron.save();
        model.put("patron", patron);
        model.put("template", "templates/patron.vtl");
      }
      model.put("books", Book.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());



    post("/book/new", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      String title = request.queryParams("title");
      String author = request.queryParams("author");
      Book book = new Book(title, author);
      book.save();
      model.put("books", Book.all());
      model.put("template", "templates/patron.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/patron/:patron_id/book/checkout", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      int patronId = Integer.parseInt(request.params("patron_id"));
      Book book = Book.find(Integer.parseInt(request.queryParams("book")));
      book.checkout(patronId);
      model.put("books", Book.all());
      model.put("patron", Patron.find(patronId));
      model.put("template", "templates/patron.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/patron/:patron_id/book/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      Book book = Book.find(Integer.parseInt(request.params("id")));
      model.put("book", book);
      model.put("template", "templates/book.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/patron/:patron_id/book/:id/renew", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      Book book = Book.find(Integer.parseInt(request.params("id")));
      book.renew();
      model.put("book", book);
      model.put("template", "templates/book.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
