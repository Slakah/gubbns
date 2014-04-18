package components

import services._
import repositories.PostRepositoryComponent


trait Default extends PlayCouch
with Pegdown
with Cache{
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