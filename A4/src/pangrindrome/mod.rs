use std::collections::HashSet;

#[allow(dead_code)]
pub fn is_palindrome(s: &str) -> bool {
    s == s.chars().rev().collect::<String>()
}

#[allow(dead_code)]
pub fn is_pangram(s: &str) -> bool {
    let res = s
        .chars()
        .flat_map(|x| x.to_lowercase())
        .filter(|&x| x >= 'a' && x <= 'z')
        .fold(HashSet::new(), |mut letters, chr| {
            letters.insert(chr);
            letters
        });
    res.len() == 26
}

#[cfg(test)]
mod tests {
    use crate::pangrindrome::{is_palindrome, is_pangram};

    #[test]
    fn basic_is_palindrome() {
        assert_eq!(true, is_palindrome("r"));
        assert_eq!(true, is_palindrome("abba"));
        assert_eq!(true, is_palindrome("abcba"));
        assert_eq!(false, is_palindrome("abc"));
        assert_eq!(false, is_palindrome("I'm bad at rust"));
        assert_eq!(true, is_palindrome("I'm bad at rusttsur ta dab m'I"));
        assert_eq!(false, is_palindrome("a aa"));
    }

    #[test]
    fn basic_pangram() {
        let quick_brown_fox = "The quick brown fox jumps over the lazy Dog";
        assert_eq!(true, is_pangram(quick_brown_fox));
        let quick_prairie_dog = "The quick prairie dog jumps over the lazy fox";
        assert_eq!(false, is_pangram(quick_prairie_dog));
        let cheese = "abcdelwefvxcghijklmnopqrstuvwjweklfsvdtuieorjkdfxzz";
        assert_eq!(false, is_pangram(cheese));
        let b = "";
        assert_eq!(false, is_pangram(b));
        let lorem_ipsum = "Autem ullam maxime nemo debitis sit nisi dicta voluptate. Quo sunt recusandae ut dolor. Ullam voluptatibus qui ad nam. Non id sunt adipisci officiis beatae delectus. Ipsum quod ad ullam commodi porro quis quis.";
        assert_eq!(false, is_pangram(lorem_ipsum));
        let a = "apoiuytrewqasdfghjdkl;'.,mnbvcxz1f234567890";
        assert_eq!(true, is_pangram(a));
    }
}
