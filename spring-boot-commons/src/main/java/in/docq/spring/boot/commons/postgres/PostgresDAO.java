package in.docq.spring.boot.commons.postgres;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class PostgresDAO {
    protected final JdbcTemplate jdbcTemplate;
    protected final ExecutorService executorService;
    protected final TransactionTemplate transactionTemplate;
    private final MeterRegistry meterRegistry;
    private final DataSource dataSource;
    private static final String DB_QUERY_TIMES = "db_query_times";
    private static final String DB_NAME = "db_name";
    private static final String DB_METRICS_GROUP = "db_metrics_group";
    private static final String DB_OPERATION_NAME = "db_operation_name";

    public PostgresDAO(DataSource dataSource, int poolSize, MeterRegistry meterRegistry) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.meterRegistry = meterRegistry;
        this.transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(this.jdbcTemplate.getDataSource()));
        this.executorService = this.getExecutorService(poolSize);
    }

    private ExecutorService getExecutorService(int poolSize) {
        return new ThreadPoolExecutor(poolSize, poolSize,
                0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private AtomicInteger next = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "postgres-thread-" + next.incrementAndGet());
                    }
                });
    }

    public CompletionStage<Integer> update(String dbMetricsGroupName, String dbOperationName, String sql, Object... args) {
        return decorateWithMetrics(dbMetricsGroupName, dbOperationName, supplyAsync(() -> jdbcTemplate.update(sql, args), executorService));
    }

    public <T> CompletionStage<Optional<T>> queryForOptionalObject(String dbMetricsGroupName, String dbOperationName, String sql, RowMapper<T> rm, Object... args) {
        return decorateWithMetrics(dbMetricsGroupName, dbOperationName, supplyAsync(() -> jdbcTemplate.queryForObject(sql, rm, args), executorService)
                .thenApply(Optional::of)
                .exceptionally(throwable -> {
                    throwable = throwable.getCause();
                    if (throwable instanceof EmptyResultDataAccessException) {
                        return Optional.empty();
                    }
                    throw new CompletionException(throwable);
                }));
    }

    public <T> CompletionStage<T> queryForObject(String dbMetricsGroupName, String dbOperationName, String sql, RowMapper<T> rm, Object... args) {
        return decorateWithMetrics(dbMetricsGroupName, dbOperationName, supplyAsync(() -> jdbcTemplate.queryForObject(sql, rm, args), executorService));
    }

    private <T> CompletionStage<T> decorateWithMetrics(String dbMetricsGroupName, String dbOperationName, CompletionStage<T> queryFuture) {
        Long start = System.currentTimeMillis();
        return queryFuture.handle((unused, throwable) -> {
            Long end = System.currentTimeMillis();
            if (throwable == null) {
                pushMetrics(dbMetricsGroupName, dbOperationName, end - start);
                return unused;
            }
            throw new CompletionException(throwable.getCause());
        });
    }

    private String getDBName() {
        try {
            String url = dataSource.unwrap(BasicDataSource.class)
                    .getUrl();
            URI uri = URI.create(url.substring(5));
            return uri.getPath();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void pushMetrics(String dbMetricsGroup, String operationName, Long elapsedTime) {
        meterRegistry.timer(DB_QUERY_TIMES, List.of(
                Tag.of(DB_NAME, getDBName()),
                Tag.of(DB_METRICS_GROUP, dbMetricsGroup),
                Tag.of(DB_OPERATION_NAME, operationName)
                )).record(elapsedTime, TimeUnit.MILLISECONDS);
    }


}
