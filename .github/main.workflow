workflow "Build and deploy" {
  on = "push"
  resolves = ["Build"]
}

action "Build" {
  uses = "MrRamych/gradle-actions/openjdk-8@2.1"
  args = "build"
}
