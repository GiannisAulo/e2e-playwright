package utils;

import config.EnvDataConfig;
import config.TestDataConfig;

import java.sql.Statement;

import static database.Connection.*;

public class DBManagement {
    public EnvDataConfig envDataConfig = new EnvDataConfig();
    public TestDataConfig testDataConfig = new TestDataConfig();

    public void cleanupSourceOfEmissionFactorElectricityData() {
        java.sql.Connection conn = connect(envDataConfig.getDbUrl(), envDataConfig.getDbUsername(),
                envDataConfig.getDbPassword());
        Statement stmt = statement(conn);

        executeUpdateQuery(stmt, "DELETE EMISSION_FACTOR_SOURCE_ELECTR_DESC");
        executeUpdateQuery(stmt, "DELETE EMISSION_FACTOR_SOURCE_ELECTR");
        executeUpdateQuery(stmt, "DELETE EMISSION_FACTOR_SOURCES_ELECTR_DRAFT");
        executeUpdateQuery(stmt, "DELETE EMISSION_FACTOR_SOURCES_ELECTR_DRAFT_DESC");

        commit(stmt);
        disconnect(stmt, conn);
    }
}
