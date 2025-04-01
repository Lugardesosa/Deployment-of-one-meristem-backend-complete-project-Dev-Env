package org.meristem.oneapp.usersservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends CrudRepository<T, ID>, PagingAndSortingRepository<T, ID> {

//    boolean existsById(@NonNull ID id);

}
