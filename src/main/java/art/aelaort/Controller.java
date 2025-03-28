package art.aelaort;

import art.aelaort.dto.NewTask;
import art.aelaort.dto.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class Controller {
	private final Repo repo;

	@GetMapping("tasks")
	public Set<Task> getTasks() {
		return repo.getTasks();
	}

	@PostMapping("tasks")
	public Long addTask(@RequestBody NewTask content) {
		return repo.addTask(content.content());
	}

	@DeleteMapping("tasks")
	public void deleteTask(@RequestParam long taskId) {
		repo.markTaskAsDeleted(taskId);
	}

	@DeleteMapping("archive")
	public void deleteArchiveTask(@RequestParam long taskId) {
		repo.deleteTaskFromArchive(taskId);
	}
}
