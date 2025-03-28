package art.aelaort;

import art.aelaort.dto.Task;
import art.aelaort.tables.Tasks;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.jooq.Records.mapping;

@Component
@RequiredArgsConstructor
public class Repo {
	private final DSLContext db;
	private final Tasks t = Tables.TASKS;

	public Set<Task> getArchiveTasks() {
		return db.select(t.ID, t.CONTENT)
				.from(t)
				.where(t.IS_DELETED.eq(true))
				.fetchSet(mapping(Task::new));
	}

	@Cacheable("tasks")
	public Set<Task> getTasks() {
		return db.select(t.ID, t.CONTENT)
				.from(t)
				.where(t.IS_DELETED.eq(false))
				.fetchSet(mapping(Task::new));
	}

	@CacheEvict(value = "tasks", allEntries = true)
	public Long addTask(String content) {
		return db.insertInto(t)
				.columns(t.CONTENT)
				.values(content)
				.returningResult(t.ID)
				.fetchOne(t.ID);
	}

	@CacheEvict(value = "tasks", allEntries = true)
	public void markTaskAsDeleted(long taskId) {
		db.update(t)
				.set(t.IS_DELETED, true)
				.where(t.ID.eq(taskId))
				.execute();
	}

	@CacheEvict(value = "tasks", allEntries = true)
	public void deleteTaskFromArchive(long taskId) {
		db.delete(t).where(t.ID.eq(taskId)).execute();
	}
}
