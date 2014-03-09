package components

import services.{CouchBlogServiceComponent, CouchServiceComponent, PostServiceComponent}
import repositories.PostRepositoryComponent

trait Default extends PlayCouch with CouchBlogServiceComponent
with PostServiceComponent with PostRepositoryComponent {
  override val couchBlog = new CouchBlogService
  override val posts = new PostService
  override val postRepository = new CouchPosts
}


trait PlayCouch extends CouchServiceComponent {
  override val couchService = new PlayCouchService
}