package components

import services._
import repositories.PostRepositoryComponent


trait Default extends PlayCouch
with Pegdown {
}


trait PlayCouch extends CouchServiceComponent with CouchBlogServiceComponent
with PostServiceComponent with PostRepositoryComponent {
  override val couchService = new PlayCouchService
  override val couchBlog = new CouchBlogService
  override val posts = new PostService
  override val postRepository = new CouchPosts
}

trait Pegdown extends MarkdownServiceComponent with PegdownServiceComponent {
  override implicit val markdown = PegdownService
}
