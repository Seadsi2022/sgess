import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISetablissement } from '../setablissement.model';

@Component({
  selector: 'jhi-setablissement-detail',
  templateUrl: './setablissement-detail.component.html',
})
export class SetablissementDetailComponent implements OnInit {
  setablissement: ISetablissement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ setablissement }) => {
      this.setablissement = setablissement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
