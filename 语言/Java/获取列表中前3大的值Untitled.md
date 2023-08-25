对于列表中元素，如果有多个字段需要比较，那么使用comparator自定义比较器是一个比较明智的选择。

对于一个大的列表，如果只想取排序前几的元素，直接排序会造成大量的计算浪费。使用最大化堆，优先队列等方式可以缩短排序时间

```Java
    public static <T> List<T> getTopN(List<T> tempList, Comparator<T> comparator, Integer limit) {
        PriorityQueue<T> queue = new PriorityQueue<>(comparator);
        for (T item : tempList) {
            queue.offer(item);
            if (queue.size() > limit) {
                queue.poll();
            }
        }

        LinkedList<T> result = new LinkedList<>();
        while (queue.size() > 0) {
            result.push(queue.poll());
        }
        return result;
    }
```