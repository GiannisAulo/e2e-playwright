package utils;

import config.EnvDataConfig;
import config.TestDataConfig;

import java.sql.Statement;

import static database.Connection.*;

public class DBManagement {
    public EnvDataConfig envDataConfig = new EnvDataConfig();
    public TestDataConfig testDataConfig = new TestDataConfig();

    public void updateDeclarantAccountDetails() {
        java.sql.Connection conn = connect(envDataConfig.getDbUrl(), envDataConfig.getDbUsername(),
                envDataConfig.getDbPassword());
        Statement stmt = statement(conn);
        executeUpdateQuery(stmt, "UPDATE declarant_account SET cbam_communication_data = JSON_MERGEPATCH(cbam_communication_data,'{\"phone\": \"\"}') WHERE JSON_VALUE(cbam_communication_data, '$.email') = 'test@netcompany.com'");
        commit(stmt);
        disconnect(stmt, conn);
    }

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

    public void cleanupNcaAuthoritiesManagementData() {
        java.sql.Connection conn = connect(envDataConfig.getDbUrl(), envDataConfig.getDbUsername(),
                envDataConfig.getDbPassword());
        Statement stmt = statement(conn);

        executeUpdateQuery(stmt, "DELETE FROM NCA_AUTHORITY");
        executeUpdateQuery(stmt, "DELETE FROM NCA_AUTHORITY_LOG");
        commit(stmt);
        disconnect(stmt, conn);
    }

    public void cleanupSurveillanceData() {
        java.sql.Connection conn = connect(envDataConfig.getDbUrl(), envDataConfig.getDbUsername(),
                envDataConfig.getDbPassword());
        Statement stmt = statement(conn);

        executeUpdateQuery(stmt, "delete from IMP_DATA_ENTRY");
        executeUpdateQuery(stmt, "delete from IMP_DATA_QTY_ENTRY");
        executeUpdateQuery(stmt, "DELETE FROM upload_imp_decl_file");
        commit(stmt);
        disconnect(stmt, conn);
    }
}
