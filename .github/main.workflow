workflow "New workflow" {
  resolves = ["Build and Deploy"]
  on = "push"
}

action "Build and Deploy" {
  uses = "MrRamych/gradle-actions/openjdk-8@2.1"
  args = "build"
}
