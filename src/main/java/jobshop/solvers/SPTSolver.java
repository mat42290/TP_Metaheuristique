package jobshop.solvers;

import jobshop.Instance;
import jobshop.Result;
import jobshop.Solver;
import jobshop.encodings.Task;
import jobshop.encodings.ResourceOrder;
import java.util.HashSet;

public class SPTSolver implements Solver {
    @Override
    public Result solve(Instance instance, long deadline) {

        ResourceOrder sol = new ResourceOrder(instance);

        HashSet<Task> readyTasks = new HashSet<Task>();

        // On initialise les tâches réalisables avec la première tâche de chaque job
        for(int job=0; job<instance.numJobs; job++) {
            Task tmp = new Task(job,0);
            readyTasks.add(tmp);
        }

        // Boucle tant qu'il y a des tâches réalisables
        while(readyTasks.size() > 0 && deadline - System.currentTimeMillis() > 1) {

            // On choisit une tâche et on la place sur la ressource associée
            Task SPT = null;
            for(Task task : readyTasks) {
                if(SPT == null) {
                    SPT = task;
                } else {
                    if(instance.duration(task) < instance.duration(SPT)) {
                        SPT = task;
                    }
                }
            }
            sol.addTask(instance.machine(SPT), SPT.job, SPT.task);

            // On met à jour l'ensemble des tâches réalisables
            readyTasks.remove(SPT);
            if(SPT.task < instance.numTasks-1) {
                Task newTask = new Task(SPT.job, SPT.task + 1);
                readyTasks.add(newTask);
            }

        }

        return new Result(instance, sol.toSchedule(), Result.ExitCause.Blocked);
    }
}