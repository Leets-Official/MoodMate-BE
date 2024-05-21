package com.moodmate.moodmatebe.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Keyword {
    축구("축구"),
    야구("야구"),
    독서("독서"),
    영화("영화"),
    음악("음악"),
    게임("게임"),
    고양이("고양이"),
    강아지("강아지"),
    패션("패션"),
    산책("산책"),
    맛집("맛집"),
    분좋카("분좋카"),
    주식("주식"),
    요리("요리"),
    헬스("헬스"),
    여행("여행"),
    애니("애니"),
    워커홀릭("워커홀릭"),
    집순돌이("집순돌이"),
    댄스("댄스"),
    애주가("애주가");

    private final String keyword;
}
