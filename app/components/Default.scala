package components

import services._
import repositories.PostRepositoryComponent


trait Default extends PlayCouch with CouchBlogServiceComponent
with PostServiceComponent with PostRepositoryComponent
with MarkdownServiceComponent with PegdownServiceComponent
with Cache{
  override val couchBlog = new CouchBlogService
  override val posts = new PostService
  override val postRepository = new CouchPosts
  override implicit val markdown = PegdownService

}


trait PlayCouch extends CouchServiceComponent {
  override val couchService = new PlayCouchService
}

trait Cache {
  import org.joda.time.Minutes
  import play.api.mvc.EssentialAction
  import play.api.cache.Cached
  import play.api.Play.current

  val longDuration = Minutes.minutes(10).toStandardSeconds.getSeconds

  def longCache(key: String)(action: EssentialAction) = {
    Cached(key, longDuration)(action)
  }
}