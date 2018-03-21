package com.keven.test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author keven
 * @date 2018-03-21 下午2:50
 * @Description fork join 测试， 需求，使用 fork/Join 计算 1 + 2 + 3 + 4 + 5
 */
public class CountTask extends RecursiveTask<Integer> {

    private static final Integer THREAD_HOLD = 2;

    private Integer start;

    private Integer end;


    public CountTask(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        boolean canComputer = (end - start) <= THREAD_HOLD;
        if (canComputer) {
            for (int i = start; i <= end; i++) {
                sum = sum + i;
            }
        } else {
            int middle = (start + end) / 2;
            CountTask left = new CountTask(start, middle);
            CountTask right = new CountTask(middle+1, end);
            //执行子任务
            left.fork();
            right.fork();

            //获取子任务结果
            sum = left.join() + right.join();
        }
        return sum;
    }


    public static void main(String[] args) throws Exception{
        ForkJoinPool joinPool = new ForkJoinPool();
        CountTask countTask = new CountTask(1, 5);

        Future<Integer> result = joinPool.submit(countTask);
        System.out.println("result = " + result.get());

    }
}
