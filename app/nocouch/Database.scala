package nocouch

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient

class Database() {

}

object Database {
  def apply(name: String) = {
    new Database()
  }
}