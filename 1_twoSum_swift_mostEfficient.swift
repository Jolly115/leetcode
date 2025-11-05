

import Foundation

func twoSum(_ nums: [Int], _ target: Int) -> [Int]? {
    var map = [Int: Int]()
    for (i, num) in nums.enumerated() {
        let complement = target - num
        if let j = map[complement] {
            return [j, i]
        }
        map[num] = i
    }
    return nil
}

// Example usage
if let result = twoSum([1, 2, 3, 4, 5], 9) {
    print(result)
} else {
    print("No match found")
}

