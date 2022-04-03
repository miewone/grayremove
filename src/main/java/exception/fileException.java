package exception;

public class fileException  extends RuntimeException {
    public Throwable nonExistPath()
    {
        return new Throwable("이 경로는 정확한 경로가 아닙니다.");
    }
}
