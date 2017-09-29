package utility;

public class Queue<T>
{

		private Record<T> begin = null;

		private Record<T> end = null;
		
		private class Record<T>
		{

	 		public T elem;

	 		public Record<T> next;

			public Record (T e) 
			{
				this.elem = e; 
				this.next = null;
			}
		}
		

		public boolean isEmpty() 
		{
			return this.begin == null;
		}

		public void enqueue(T e) 
		{
			if (this.isEmpty())
				this.begin = this.end = new Record<T>(e);
			else 
			{
				this.end.next = new Record<T>(e);
				this.end = this.end.next;
			}
		}


		public Object first() throws EmptyQueueException
		{
			if (this.begin == null)
				throw new EmptyQueueException("Errore, impossibile leggere la testa di una coda vuota");
			return this.begin.elem;
		}

		public void dequeue() throws EmptyQueueException
		{
			if(this.begin==this.end)
			{
				if(this.begin==null)
					throw new EmptyQueueException("Errore, impossibile cancellare la testa di una coda vuota");
				else
					this.begin=this.end=null;
			}
			else
			{
				begin=begin.next;
			}
			
		}

	}