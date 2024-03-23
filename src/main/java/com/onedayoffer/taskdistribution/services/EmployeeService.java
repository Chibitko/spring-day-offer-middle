package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import com.onedayoffer.taskdistribution.DTO.TaskStatus;
import com.onedayoffer.taskdistribution.exceptions.EntityNotFoundException;
import com.onedayoffer.taskdistribution.repositories.EmployeeRepository;
import com.onedayoffer.taskdistribution.repositories.TaskRepository;
import com.onedayoffer.taskdistribution.repositories.entities.Employee;
import com.onedayoffer.taskdistribution.repositories.entities.Task;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public List<EmployeeDTO> getEmployees(@Nullable String sortDirection) {
        Sort.Direction direction = "ASC".equals(sortDirection)
                ? Sort.Direction.ASC
                : "DESC".equals(sortDirection)
                    ? Sort.Direction.DESC
                    : null;

        List<Employee> employees = direction == null
                ? employeeRepository.findAll()
                : employeeRepository.findAllAndSort(Sort.by(direction, "fio"));

        Type listType = new TypeToken<List<EmployeeDTO>>() {}.getType();
        List<EmployeeDTO> employeeDTOS = modelMapper.map(employees, listType);

        return employeeDTOS;
    }

    //@Transactional ???
    public EmployeeDTO getOneEmployee(Integer id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            return null;
        }

        EmployeeDTO employeeDTO = modelMapper.map(employee.get(), EmployeeDTO.class);

        return employeeDTO;
    }

    public List<TaskDTO> getTasksByEmployeeId(Integer id) {
        List<Task> tasks = taskRepository.findTasksByEmployeeId(id);

        Type listType = new TypeToken<List<TaskDTO>>() {}.getType();
        List<TaskDTO> taskDTOS = modelMapper.map(tasks, listType);

        return taskDTOS;
    }

    @Transactional
    public void changeTaskStatus(Integer taskId, TaskStatus status) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            return; // Идемпотентность!!!
        }

        var task = taskOptional.get();
        if (task.getStatus() == status) {
            return; // Идемпотентность!!!
        }

        task.setStatus(status);

        taskRepository.save(task);
    }

    @Transactional
    public void postNewTask(Integer employeeId, TaskDTO newTask) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isEmpty()) {
            throw new EntityNotFoundException("Employee " + employeeId + " not found");
        }

        Task task = new Task();

        modelMapper.map(newTask, task);

        task.setEmployee(employee.get());

        taskRepository.save(task);
    }
}
