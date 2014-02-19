package db


trait MockConfigService extends ConfigService {
  override def config(): Config = Config()
}
