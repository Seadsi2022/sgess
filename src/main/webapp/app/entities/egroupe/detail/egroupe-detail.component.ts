import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEgroupe } from '../egroupe.model';

@Component({
  selector: 'jhi-egroupe-detail',
  templateUrl: './egroupe-detail.component.html',
})
export class EgroupeDetailComponent implements OnInit {
  egroupe: IEgroupe | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egroupe }) => {
      this.egroupe = egroupe;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
