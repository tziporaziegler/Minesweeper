[![GitHub release](https://img.shields.io/github/release/tziporaziegler/minesweeper.svg)][3]
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2e3173ec6ec641f898fc93486d3e2043)](https://www.codacy.com/app/tziporaziegler/Minesweeper?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=tziporaziegler/Minesweeper&amp;utm_campaign=Badge_Grade)
[![Code Climate](https://codeclimate.com/github/tziporaziegler/Minesweeper/badges/gpa.svg)](https://codeclimate.com/github/tziporaziegler/Minesweeper)

# Minesweeper

This Minesweeper game is enhanced version of the classic
[Microsoft Minesweeper][1] game.

Features include:

- Beginner, Intermediate and Expert level options
- Timer and bombs left displays that change color every 10 seconds
- Too many flags display warning
- Fireworks when you win!

![WinScreenshot][2]

## How to Play

The goal of the game is to uncover all cells on the board that do not contain
mines without being "blown up" by clicking on a square with a mine underneath.

The location of the mines is discovered by a process of logic. Clicking on the
game board will reveal what is hidden underneath the chosen cells or cells
(a large number of blank cells may be revealed in one go if they are adjacent to
each other). Some squares are blank but some contain numbers (1 to 8), each
number being the number of mines adjacent to the uncovered square.

To help avoid hitting a mine, the location of a suspected mine can be marked by
flagging it with the right mouse button. The amount of flags you can place is
limited to the amount of bombs in game. If you attempt to exceed the number, the
bombs display flashes yellow, and you must remove a flag before you can place a
new one. Once you have flagged all the bombs around an open cell, you can
quickly open the remaining non-bomb cells by right + left clicking on the cell.

The game is won once all blank squares have been uncovered without hitting a
mine, any remaining mines not identified by flags being automatically flagged
by the computer. However, in the event that a game is lost and the player
mistakenly flags a safe square, that square will either
appear with a red X covering the mine (denoting it as safe)
or just a red X (also denoting it as safe).

You can start a new game at any point by clicking on the smiley face button.

Happy mine hunting!

## Downloading

Runnable `.jar` files for Mac and Windows are available for download
in the [latest release][3].

To download or clone a complete copy of the source code, use Git or checkout
with SVN using `https://github.com/tziporaziegler/Minesweeper.git`
web URL. A `.zip` and `tar.gz` file is also available in the latest release.

[1]:https://en.wikipedia.org/wiki/Microsoft_Minesweeper
[2]:https://github.com/tziporaziegler/Minesweeper/blob/master/screenshots/ExpWin256.png
[3]:https://github.com/tziporaziegler/Minesweeper/releases/latest
