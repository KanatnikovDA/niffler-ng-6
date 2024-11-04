package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.jdbc.Connections;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.data.jpa.EntityManagers;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabasesExtension implements SuiteExtension {

  private static final Config CFG = Config.getInstance();
  private final JdbcTemplate authJdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
  private final JdbcTemplate spendJdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
  private final JdbcTemplate userdataJdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));

  @Override
  public void afterSuite() {
    Connections.closeAllConnections();
    EntityManagers.closeAllEmfs();
  }

  @Override
  public void beforeSuite(ExtensionContext context) {
    clearDatabaseTables();
  }

  @Step("Удаляем БД: auth; spend; userdata")
  private void clearDatabaseTables() {
    authJdbcTemplate.execute("TRUNCATE TABLE authority, \"user\" CASCADE;");
    spendJdbcTemplate.execute("TRUNCATE TABLE category, spend CASCADE;");
    userdataJdbcTemplate.execute("TRUNCATE TABLE friendship, \"user\" CASCADE;");
  }
}
