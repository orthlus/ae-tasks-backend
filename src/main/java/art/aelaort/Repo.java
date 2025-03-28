package art.aelaort;

import art.aelaort.dto.Task;
import art.aelaort.tables.ArchiveTasks;
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
	private final ArchiveTasks at = Tables.ARCHIVE_TASKS;

	public Set<Task> getArchiveTasks() {
		return db.select(t.ID, t.CONTENT)
				.from(at)
				.fetchSet(mapping(Task::new));
	}

	@Cacheable("tasks")
	public Set<Task> getTasks() {
		return db.select(t.ID, t.CONTENT)
				.from(t)
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
	public void moveToArchive(long taskId) {
		db.transaction(c -> {
			DSLContext dsl = c.dsl();
			dsl.insertInto(at)
					.select(dsl.selectFrom(t).where(t.ID.eq(taskId)))
					.execute();
			dsl.delete(t).where(t.ID.eq(taskId)).execute();
		});
	}

	public void deleteTaskFromArchive(long taskId) {
		db.delete(at)
				.where(t.ID.eq(taskId))
				.execute();
	}

	public void deleteAllArchiveTasks() {
		db.delete(at).execute();
	}
}
