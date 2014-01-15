package controllers

import play.api.mvc.{Action, Controller}
import models.{Post, PostRepository}
import util.BlogDB
import play.api.data._
import play.api.data.Forms._
import org.joda.time.DateTime
import play.Logger
import java.util.UUID


object Blog extends Controller {
  val postRepo = new PostRepository(BlogDB.db)

  def index = Action {
    val posts = postRepo.all
    Ok(views.html.blog.list.render(posts))
  }

  def title(name: String) = Action {
    val postsByTitle = postRepo.byTitle(name)
    if (postsByTitle.isEmpty)
      NotFound

    val post = postsByTitle(0)
    val posts = postRepo.all
    val postIndex = posts.indexOf(post)

    val nextPost = posts.lift(postIndex + 1)
    val prevPost = posts.lift(postIndex - 1)

    Ok(views.html.blog.single.render(post, prevPost, nextPost))
  }

  val loginForm = Form(
    tuple(
      "title" -> text,
      "content" -> text
    )
  )

  def add() = Action {
    Ok(views.html.blog.add.render(loginForm))
  }

  def submit() = Action {implicit request =>
    val (title, content) = loginForm.bindFromRequest().get
    val post = Post(UUID.randomUUID().toString, null, "post", title, content, DateTime.now, "James Collier")
    Logger.info(post._id)
    val jsonPost = BlogDB.mapper.writeValueAsString(post)
    Logger.info("saving post." + post.title)
    postRepo.add(post)

    Ok("saved")
  }

}