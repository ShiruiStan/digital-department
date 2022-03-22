package com.atcdi.digital.handler;

import com.atcdi.digital.entity.StandardException;
import org.apache.ibatis.type.*;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@MappedTypes({Set.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class MybatisTypeHandler extends BaseTypeHandler<Set> {



    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Set s, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, StringUtils.collectionToCommaDelimitedString(s));
    }


    @Override
    public Set getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return splitToSet(resultSet.getString(s));
    }

    @Override
    public Set getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return splitToSet(resultSet.getString(i));
    }

    @Override
    public Set getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return splitToSet(callableStatement.getString(i));
    }

    private Set<String> splitToSet(String s) {
        if (s != null) {
            return new HashSet<>(Arrays.asList(s.split(",")));
        } else {
            return null;
        }
    }


}

