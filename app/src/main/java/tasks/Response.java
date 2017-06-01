package tasks;


/**
 * 使用模板类的类型安全，只是利用编译器的类型检查，来自动保证运行期的类型强转的正确与安全
 * @param <T>
 */
public class Response<T> {
	public interface Listener<T> {
		void onResponse(T result);
		
		void onErrorResponse(Exception exception);
	}
	
	public static <T> Response<T> success(T result) {
		return new Response<T>(result);
	}
	
	public static <T> Response<T> error(Exception e) {
		return new Response<T>(e);
	}
	
	private T result;
	
	private Exception exception;
	
	private Response(T result) {
		this.result = result;
	}
	
	private Response(Exception exception) {
		this.exception = exception;
	}
	
	public boolean isSuccess() {
		return exception == null;
	}

	public T getResult() {
		return result;
	}

	public Exception getException() {
		return exception;
	}
}