package db


class MockConfigService extends ConfigService {
  override def config(): Config = Config()
}
