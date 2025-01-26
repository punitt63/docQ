package in.docq.spring.boot.commons.postgres;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class PostgresDAO {
    protected final JdbcTemplate jdbcTemplate;
    protected final ExecutorService executorService;
    protected final TransactionTemplate transactionTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final DataSource dataSource;
    private final int poolSize;

    public PostgresDAO(DataSource dataSource, int poolSize, MeterRegistry meterRegistry) {
        this.poolSize = poolSize;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
        this.transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(this.jdbcTemplate.getDataSource()));
        this.executorService = this.getExecutorService(poolSize);
        this.dataSource = dataSource;
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

    public CompletionStage<Integer> update(String sql, Object... args) {
        return supplyAsync(() -> jdbcTemplate.update(sql, args), executorService);
    }

    public <T> CompletionStage<List<T>> query(String sql, RowMapper<T> rm, Object... args) {
        return supplyAsync(() -> jdbcTemplate.query(sql, rm, args), executorService);
    }

    public <T> CompletionStage<Optional<T>> queryForOptionalObject(String sql, RowMapper<T> rm, Object... args) {
        return queryForObject(sql, rm, args)
                .thenApply(Optional::of)
                .exceptionally(throwable -> {
                    throwable = throwable.getCause();
                    if (throwable instanceof EmptyResultDataAccessException) {
                        return Optional.empty();
                    }
                    throw new CompletionException(throwable);
                });
    }

    public <T> CompletionStage<T> queryForObject(String sql, RowMapper<T> rm, Object... args) {
        return supplyAsync(() -> jdbcTemplate.queryForObject(sql, rm, args), executorService);
    }


}
