package uva.se;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import uva.se.pipes.Pipeline;

public class Program {
	
	public static void main(String[] args) {
		examples();
	}
	
	public static void examples() {
		Pipeline p = new Pipeline();
		
		
		System.out.println("Push a number through a print function");
		p
			.from(() -> 5)
			.to(i -> System.out.println("Pushed " + i));
		
		p.run();
		
		
		System.out.println("Push 5 numbers through a print function");
		p
			.fromCollection(() -> Arrays.asList(1, 2, 3, 4, 5))
			.to(i -> System.out.println("Pushed " + i));
		
		p.run();
		
		
		System.out.println("Multiply all even numbers up to 25 and print");
		p
			.fromCollection(() -> intRange(0, 25))
			.filter(i -> i % 2 == 0)
			.to(i -> System.out.println("Even " + i));
		
		p.run();
		
		
		System.out.println("Duplicate input");
		p
			.fromCollection(() -> Arrays.asList(1, 2, 3))
			.expand(i -> Arrays.asList(i, i))
			.to(print());
		
		p.run();
		

		System.out.println("Integer to Float");
		p
			.from(() -> 1)
			.doVia(print())
			.via(i -> new Float(i))
			.to(print());
		
		p.run();
		
		
		System.out.println("FizzBuzz, print 1..100, mul 3 - fizz, mul 5 - buzz, mul3&5 - fizzbuzz");
		p
			.fromCollection(() -> intRange(1, 100))
			.filter(i -> { boolean fb = i % 3 == 0 && i % 5 == 0; if (fb) System.out.println("FizzBuzz"); return !fb; })
			.filter(i -> { boolean fb = i % 5 == 0; if (fb) System.out.println("Buzz"); return !fb; })
			.filter(i -> { boolean fb = i % 3 == 0; if (fb) System.out.println("Fizz"); return !fb; })
			.to(i -> System.out.println(i));
		
		p.run();
	}
	
	private static List<Integer> intRange(int incLower, int incUpper) {
		return IntStream.rangeClosed(incLower, incUpper).boxed().collect(Collectors.toList());
	}
	
	public static <T> Consumer<T> print() {
		return t -> System.out.println(t);
	}
}