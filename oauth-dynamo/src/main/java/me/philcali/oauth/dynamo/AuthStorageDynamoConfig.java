package me.philcali.oauth.dynamo;

public class AuthStorageDynamoConfig {
    private static final String DEFAULT_PREFIX = "OAuth";
    private String tablePrefix = DEFAULT_PREFIX;
    private String nonceTableName = "Nonces";
    private String applicationTableName = "Applications";
    private String tokenTableName = "Tokens";

    private String buildTable(final String tableName) {
        return String.format("%s.%s", getTablePrefix(), tableName);
    }

    public String getApplicationTableName() {
        return buildTable(applicationTableName);
    }

    public String getNonceTableName() {
        return buildTable(nonceTableName);
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public String getTokenTableName() {
        return buildTable(tokenTableName);
    }

    public void setApplicationTableName(final String applicationTableName) {
        this.applicationTableName = applicationTableName;
    }

    public void setNonceTableName(final String nonceTableName) {
        this.nonceTableName = nonceTableName;
    }

    public void setTablePrefix(final String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public void setTokenTableName(final String tokenTableName) {
        this.tokenTableName = tokenTableName;
    }

}
