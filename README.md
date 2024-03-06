<div align="center">
    <h1>PenKaTur Core</h1>
    <h3>Sports management of an amateur</h3>
</div>

PenKaTur Core is a powerful API designed for athletes looking to optimize their performance through personalized nutritional planning. With a focus on helping users achieve their goals of weight, calories, and macronutrients.

PenKaTur Core offers a comprehensive solution for managing nutrition based on planned workouts. With advanced features such as custom goal settings and automatic generation of meal plans based on each user's individual needs.

PenKaTur Core allows athletes to maximize their performance and achieve their goals efficiently. Whether you're training for a competition or simply looking to improve your overall health and well-being, PenKaTur Core is the perfect tool to take your performance to the next level.


## Deploy

<p align="center">
  <a href="https://hub.docker.com/u/bitwarden/" target="_blank">
    <img src="https://i.imgur.com/SZc8JnH.png" alt="docker" />
  </a>
</p>

You can deploy PenKaTur using Docker containers on Windows, macOS, and Linux distributions. Use the provided PowerShell and Bash scripts to get started quickly. Find all of the PenKaTur images on [Docker Hub](https://hub.docker.com/r/penkatur/core).

### Requirements

- [Docker](https://www.docker.com/community-edition#/download)
- [Docker Compose](https://docs.docker.com/compose/install/) (already included with some Docker installations)

_These dependencies are free to use._

```sh
docker pull penkatur/core:nightly
docker run -d --name PenKaTur --restart unless-stopped -p 8080:8080 penkatur/core:nightly
```


## Contribute

Code contributions are welcome! Please commit any pull requests against the `main` branch.