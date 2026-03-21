package org.meristem.oneapp.walletservice.repositories;

import org.meristem.oneapp.walletservice.models.OutboxEvent;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface OutboxEventRepository extends BaseRepository<OutboxEvent, Long> {

    List<OutboxEvent> findAllByStatus(Integer status, Sort sort, Limit limit);

    List<OutboxEvent> findAllByOutboxStatus(Integer outboxStatus, Sort createdDate, Limit limit);
}
