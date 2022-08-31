import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEeventualitevariable } from '../eeventualitevariable.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../eeventualitevariable.test-samples';

import { EeventualitevariableService } from './eeventualitevariable.service';

const requireRestSample: IEeventualitevariable = {
  ...sampleWithRequiredData,
};

describe('Eeventualitevariable Service', () => {
  let service: EeventualitevariableService;
  let httpMock: HttpTestingController;
  let expectedResult: IEeventualitevariable | IEeventualitevariable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EeventualitevariableService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Eeventualitevariable', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const eeventualitevariable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(eeventualitevariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Eeventualitevariable', () => {
      const eeventualitevariable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(eeventualitevariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Eeventualitevariable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Eeventualitevariable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Eeventualitevariable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEeventualitevariableToCollectionIfMissing', () => {
      it('should add a Eeventualitevariable to an empty array', () => {
        const eeventualitevariable: IEeventualitevariable = sampleWithRequiredData;
        expectedResult = service.addEeventualitevariableToCollectionIfMissing([], eeventualitevariable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eeventualitevariable);
      });

      it('should not add a Eeventualitevariable to an array that contains it', () => {
        const eeventualitevariable: IEeventualitevariable = sampleWithRequiredData;
        const eeventualitevariableCollection: IEeventualitevariable[] = [
          {
            ...eeventualitevariable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEeventualitevariableToCollectionIfMissing(eeventualitevariableCollection, eeventualitevariable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Eeventualitevariable to an array that doesn't contain it", () => {
        const eeventualitevariable: IEeventualitevariable = sampleWithRequiredData;
        const eeventualitevariableCollection: IEeventualitevariable[] = [sampleWithPartialData];
        expectedResult = service.addEeventualitevariableToCollectionIfMissing(eeventualitevariableCollection, eeventualitevariable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eeventualitevariable);
      });

      it('should add only unique Eeventualitevariable to an array', () => {
        const eeventualitevariableArray: IEeventualitevariable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const eeventualitevariableCollection: IEeventualitevariable[] = [sampleWithRequiredData];
        expectedResult = service.addEeventualitevariableToCollectionIfMissing(eeventualitevariableCollection, ...eeventualitevariableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eeventualitevariable: IEeventualitevariable = sampleWithRequiredData;
        const eeventualitevariable2: IEeventualitevariable = sampleWithPartialData;
        expectedResult = service.addEeventualitevariableToCollectionIfMissing([], eeventualitevariable, eeventualitevariable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eeventualitevariable);
        expect(expectedResult).toContain(eeventualitevariable2);
      });

      it('should accept null and undefined values', () => {
        const eeventualitevariable: IEeventualitevariable = sampleWithRequiredData;
        expectedResult = service.addEeventualitevariableToCollectionIfMissing([], null, eeventualitevariable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eeventualitevariable);
      });

      it('should return initial array if no Eeventualitevariable is added', () => {
        const eeventualitevariableCollection: IEeventualitevariable[] = [sampleWithRequiredData];
        expectedResult = service.addEeventualitevariableToCollectionIfMissing(eeventualitevariableCollection, undefined, null);
        expect(expectedResult).toEqual(eeventualitevariableCollection);
      });
    });

    describe('compareEeventualitevariable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEeventualitevariable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEeventualitevariable(entity1, entity2);
        const compareResult2 = service.compareEeventualitevariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEeventualitevariable(entity1, entity2);
        const compareResult2 = service.compareEeventualitevariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEeventualitevariable(entity1, entity2);
        const compareResult2 = service.compareEeventualitevariable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
