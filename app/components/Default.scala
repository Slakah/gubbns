package components

import services.{CouchBlogServiceComponent, CouchServiceComponent, PostServiceComponent}
import repositories.PostRepositoryComponent

trait Default extends CouchServiceComponent with CouchBlogServiceComponent
with PostServiceComponent with PostRepositoryComponent {

  override val couchService = new PlayCouchService
  override val couchBlog = new CouchBlogService
  override val posts = new PostService
  override val postRepository = new CouchPosts
}
