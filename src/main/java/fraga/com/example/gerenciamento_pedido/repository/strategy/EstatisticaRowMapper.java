package fraga.com.example.gerenciamento_pedido.repository.strategy;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;


public class EstatisticaRowMapper<T> implements RowMapper<T> {

    private final Class<T> type;
    private final Function<ResultSet, T> mapperFunction;

    public EstatisticaRowMapper(Class<T> type, Function<ResultSet, T> mapperFunction) {
        this.type = type;
        this.mapperFunction = mapperFunction;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        return mapperFunction.apply(rs);
    }

}
