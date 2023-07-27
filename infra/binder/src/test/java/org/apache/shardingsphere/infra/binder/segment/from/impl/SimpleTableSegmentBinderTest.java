/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.infra.binder.segment.from.impl;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.shardingsphere.infra.binder.segment.from.TableSegmentBinderContext;
import org.apache.shardingsphere.infra.database.core.DefaultDatabase;
import org.apache.shardingsphere.infra.database.mysql.MySQLDatabaseType;
import org.apache.shardingsphere.infra.database.postgresql.PostgreSQLDatabaseType;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.infra.metadata.database.schema.model.ShardingSphereSchema;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.item.ColumnProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.OwnerSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.TableNameSegment;
import org.apache.shardingsphere.sql.parser.sql.common.value.identifier.IdentifierValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleTableSegmentBinderTest {
    
    @Test
    void assertBind() {
        SimpleTableSegment simpleTableSegment = new SimpleTableSegment(new TableNameSegment(0, 10, new IdentifierValue("t_order")));
        ShardingSphereMetaData metaData = createMetaData();
        Map<String, TableSegmentBinderContext> tableBinderContexts = new CaseInsensitiveMap<>();
        SimpleTableSegment actual = SimpleTableSegmentBinder.bind(simpleTableSegment, metaData, DefaultDatabase.LOGIC_NAME, new MySQLDatabaseType(), tableBinderContexts);
        assertThat(actual.getTableName().getOriginalDatabase().getValue(), is(DefaultDatabase.LOGIC_NAME));
        assertThat(actual.getTableName().getOriginalSchema().getValue(), is(DefaultDatabase.LOGIC_NAME));
        assertTrue(tableBinderContexts.containsKey("t_order"));
        assertThat(tableBinderContexts.get("t_order").getProjectionSegments().size(), is(3));
        assertTrue(tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("user_id") instanceof ColumnProjectionSegment);
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("user_id")).getColumn().getOriginalDatabase().getValue(),
                is(DefaultDatabase.LOGIC_NAME));
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("user_id")).getColumn().getOriginalSchema().getValue(),
                is(DefaultDatabase.LOGIC_NAME));
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("user_id")).getColumn().getOriginalTable().getValue(), is("t_order"));
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("user_id")).getColumn().getOriginalColumn().getValue(), is("user_id"));
        assertTrue(tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("order_id") instanceof ColumnProjectionSegment);
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("order_id")).getColumn().getOriginalDatabase().getValue(),
                is(DefaultDatabase.LOGIC_NAME));
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("order_id")).getColumn().getOriginalSchema().getValue(),
                is(DefaultDatabase.LOGIC_NAME));
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("order_id")).getColumn().getOriginalTable().getValue(), is("t_order"));
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("order_id")).getColumn().getOriginalColumn().getValue(), is("order_id"));
        assertTrue(tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("status") instanceof ColumnProjectionSegment);
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("status")).getColumn().getOriginalDatabase().getValue(),
                is(DefaultDatabase.LOGIC_NAME));
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("status")).getColumn().getOriginalSchema().getValue(),
                is(DefaultDatabase.LOGIC_NAME));
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("status")).getColumn().getOriginalTable().getValue(), is("t_order"));
        assertThat(((ColumnProjectionSegment) tableBinderContexts.get("t_order").getProjectionSegmentByColumnLabel("status")).getColumn().getOriginalColumn().getValue(), is("status"));
    }
    
    @Test
    void assertBindWithSchemaForMySQL() {
        SimpleTableSegment simpleTableSegment = new SimpleTableSegment(new TableNameSegment(0, 10, new IdentifierValue("t_order")));
        simpleTableSegment.setOwner(new OwnerSegment(0, 0, new IdentifierValue("sharding_db")));
        ShardingSphereMetaData metaData = createMetaData();
        Map<String, TableSegmentBinderContext> tableBinderContexts = new CaseInsensitiveMap<>();
        SimpleTableSegment actual = SimpleTableSegmentBinder.bind(simpleTableSegment, metaData, DefaultDatabase.LOGIC_NAME, new MySQLDatabaseType(), tableBinderContexts);
        assertThat(actual.getTableName().getOriginalDatabase().getValue(), is("sharding_db"));
        assertThat(actual.getTableName().getOriginalSchema().getValue(), is("sharding_db"));
    }
    
    @Test
    void assertBindWithoutSchemaForMySQL() {
        SimpleTableSegment simpleTableSegment = new SimpleTableSegment(new TableNameSegment(0, 10, new IdentifierValue("t_order")));
        ShardingSphereMetaData metaData = createMetaData();
        Map<String, TableSegmentBinderContext> tableBinderContexts = new CaseInsensitiveMap<>();
        SimpleTableSegment actual = SimpleTableSegmentBinder.bind(simpleTableSegment, metaData, DefaultDatabase.LOGIC_NAME, new MySQLDatabaseType(), tableBinderContexts);
        assertThat(actual.getTableName().getOriginalDatabase().getValue(), is(DefaultDatabase.LOGIC_NAME));
        assertThat(actual.getTableName().getOriginalSchema().getValue(), is(DefaultDatabase.LOGIC_NAME));
    }
    
    @Test
    void assertBindWithSchemaForPostgreSQL() {
        SimpleTableSegment simpleTableSegment = new SimpleTableSegment(new TableNameSegment(0, 10, new IdentifierValue("t_order")));
        OwnerSegment schema = new OwnerSegment(0, 0, new IdentifierValue("test"));
        schema.setOwner(new OwnerSegment(0, 0, new IdentifierValue("sharding_db")));
        simpleTableSegment.setOwner(schema);
        ShardingSphereMetaData metaData = createMetaData();
        Map<String, TableSegmentBinderContext> tableBinderContexts = new CaseInsensitiveMap<>();
        SimpleTableSegment actual = SimpleTableSegmentBinder.bind(simpleTableSegment, metaData, DefaultDatabase.LOGIC_NAME, new PostgreSQLDatabaseType(), tableBinderContexts);
        assertThat(actual.getTableName().getOriginalDatabase().getValue(), is("sharding_db"));
        assertThat(actual.getTableName().getOriginalSchema().getValue(), is("test"));
    }
    
    @Test
    void assertBindWithoutSchemaForPostgreSQL() {
        SimpleTableSegment simpleTableSegment = new SimpleTableSegment(new TableNameSegment(0, 10, new IdentifierValue("t_order")));
        ShardingSphereMetaData metaData = createMetaData();
        Map<String, TableSegmentBinderContext> tableBinderContexts = new CaseInsensitiveMap<>();
        SimpleTableSegment actual = SimpleTableSegmentBinder.bind(simpleTableSegment, metaData, DefaultDatabase.LOGIC_NAME, new PostgreSQLDatabaseType(), tableBinderContexts);
        assertThat(actual.getTableName().getOriginalDatabase().getValue(), is(DefaultDatabase.LOGIC_NAME));
        assertThat(actual.getTableName().getOriginalSchema().getValue(), is("public"));
    }
    
    @Test
    void assertBindWhenContainsPgCatalogTableForPostgreSQL() {
        SimpleTableSegment simpleTableSegment = new SimpleTableSegment(new TableNameSegment(0, 10, new IdentifierValue("pg_database")));
        ShardingSphereMetaData metaData = createMetaData();
        Map<String, TableSegmentBinderContext> tableBinderContexts = new CaseInsensitiveMap<>();
        SimpleTableSegment actual = SimpleTableSegmentBinder.bind(simpleTableSegment, metaData, DefaultDatabase.LOGIC_NAME, new PostgreSQLDatabaseType(), tableBinderContexts);
        assertThat(actual.getTableName().getOriginalDatabase().getValue(), is(DefaultDatabase.LOGIC_NAME));
        assertThat(actual.getTableName().getOriginalSchema().getValue(), is("pg_catalog"));
    }
    
    private ShardingSphereMetaData createMetaData() {
        ShardingSphereSchema schema = mock(ShardingSphereSchema.class);
        when(schema.getAllColumnNames("t_order")).thenReturn(Arrays.asList("order_id", "user_id", "status"));
        when(schema.getAllColumnNames("pg_database")).thenReturn(Arrays.asList("datname", "datdba"));
        ShardingSphereMetaData result = mock(ShardingSphereMetaData.class, RETURNS_DEEP_STUBS);
        when(result.getDatabase(DefaultDatabase.LOGIC_NAME).getSchema(DefaultDatabase.LOGIC_NAME)).thenReturn(schema);
        when(result.getDatabase("sharding_db").getSchema("sharding_db")).thenReturn(schema);
        when(result.getDatabase(DefaultDatabase.LOGIC_NAME).getSchema("public")).thenReturn(schema);
        when(result.getDatabase("sharding_db").getSchema("test")).thenReturn(schema);
        return result;
    }
}
