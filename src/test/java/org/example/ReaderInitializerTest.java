package org.example;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReaderInitializerTest {

    @Entity
    static class MyBean {

        @Id
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Repository
    interface MyBeanRepository extends JpaRepository<MyBean, Long> {

    }

    /*
     * NOTE: methodName correctness will only be tester at runtime,
     * when org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader.read is called
     */

    @Test
    void init_reader_via_spring() throws Exception {
        MyBeanRepository myRepository = mock(MyBeanRepository.class);
        when(myRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        RepositoryItemReader<MyBean> reader = new RepositoryItemReader<>();
        reader.setRepository(myRepository);
        reader.setMethodName("findAll");
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        reader.afterPropertiesSet();
        assertThat(reader).isNotNull();
        String readerName = reader.getExecutionContextKey("bean");
        assertThat(readerName).isNotNull();
    }

    @Test
    void init_reader_via_spring_with_name() throws Exception {
        MyBeanRepository myRepository = mock(MyBeanRepository.class);
        when(myRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        RepositoryItemReader<MyBean> reader = new RepositoryItemReader<>();
        reader.setRepository(myRepository);
        reader.setMethodName("findAll");
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        reader.setName("reader-1");
        reader.afterPropertiesSet();
        assertThat(reader).isNotNull();
        String readerName = reader.getExecutionContextKey("bean");
        assertThat(readerName).isNotNull();
    }

    @Test
    void init_reader_via_spring_with_state_false() throws Exception {
        MyBeanRepository myRepository = mock(MyBeanRepository.class);
        when(myRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        RepositoryItemReader<MyBean> reader = new RepositoryItemReader<>();
        reader.setRepository(myRepository);
        reader.setMethodName("findAll");
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        reader.setSaveState(false);
        reader.afterPropertiesSet();
        assertThat(reader).isNotNull();
    }

    @Test
    void init_reader_via_builder() {
        MyBeanRepository myRepository = mock(MyBeanRepository.class);
        when(myRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        RepositoryItemReader<MyBean> reader = new RepositoryItemReaderBuilder<MyBean>()
                .repository(myRepository)
                .methodName("findAll")
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
        assertThat(reader).isNotNull();
        String readerName = reader.getExecutionContextKey("builder");
        assertThat(readerName).isNotNull();
    }

    @Test
    void init_reader_via_builder_with_name() {
        MyBeanRepository myRepository = mock(MyBeanRepository.class);
        when(myRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        RepositoryItemReader<MyBean> reader = new RepositoryItemReaderBuilder<MyBean>()
                .repository(myRepository)
                .methodName("findAll")
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .name("reader-2")
                .build();
        assertThat(reader).isNotNull();
        String readerName = reader.getExecutionContextKey("builder");
        assertThat(readerName).isNotNull();
    }

    @Test
    void init_reader_via_builder_with_state_false() {
        MyBeanRepository myRepository = mock(MyBeanRepository.class);
        when(myRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        RepositoryItemReader<MyBean> reader = new RepositoryItemReaderBuilder<MyBean>()
                .repository(myRepository)
                .methodName("findAll")
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .saveState(false)
                .build();
        assertThat(reader).isNotNull();
    }

}
