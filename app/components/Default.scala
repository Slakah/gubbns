package components

import services._
import repositories.PostRepositoryComponent


trait Default extends PlayCouch with Pegdown

trait PlayCouch extends Pegdown with Repositories with PostServiceComponent {
  override lazy val posts = new Posts with Repositories
}

trait Pegdown extends MarkdownServiceComponent with PegdownServiceComponent {
  override implicit lazy val markdown = PegdownService
}

trait Repositories extends CouchServiceComponent with CouchBlogServiceComponent with PostRepositoryComponent {
  override lazy val couchService = new PlayCouchService
  override lazy val couchBlog = new CouchBlogService
  override lazy val postRepository = new CouchPosts

}