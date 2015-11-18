package utility;

import java.lang.reflect.Array;

public class RingBuffer<T>
{
	private T[] _data;
	private int _head;
	private int _tail;
	private int _size;
	
	public RingBuffer(Class<T> type, int size)
	{
		@SuppressWarnings("unchecked")
		final T[] data = (T[])Array.newInstance(type, size);
		_data = data;
		_head = 0;
		_tail = 0;
		_size = size;
	}
	
	public T Dequeue()
	{
		if (_head == _tail)
			return null;
		
		T item = _data[_head];
		
		_head = (_head + 1) % _size;
		
		return item;
	}
	
	public void Enqueue(T item)
	{
		int newTail = (_tail + 1) % _size;
		if (newTail == _head)
		{
			Logger.Info("Buffer overflow, item [" + item + "] discarded.");
			return;
		}
		
		_data[_tail] = item;
		_tail = newTail;
	}
	
}
