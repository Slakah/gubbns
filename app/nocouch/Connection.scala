package nocouch

import org.apache.http.client.HttpClient
import org.apache.http.HttpHost
import org.apache.http.client.utils.URIBuilder

class Connection(httpClient: HttpClient, host: HttpHost) {

  def get(partialUri: URIBuilder) = {
    val uri = finaliseURI(partialUri)
  }

  private def finaliseURI(partialUri: URIBuilder) = {
    partialUri.setScheme(host.getSchemeName)
    partialUri.setHost(host.getHostName)
    partialUri.setPort(host.getPort)
    partialUri.build()
  }
}
