package models

import org.joda.time.DateTime
import play.api.mvc.Security.AuthenticatedRequest

case class PostForm(title: String, content: String) {
  def toPost[A](implicit request: AuthenticatedRequest[A, String]): Post = {
    Post(title = title, content = content, published = DateTime.now, author = request.user)
  }
}